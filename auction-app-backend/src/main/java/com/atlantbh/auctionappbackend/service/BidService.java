package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.atlantbh.auctionappbackend.exception.*;
import com.atlantbh.auctionappbackend.model.*;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.dto.UserMaxBidRecord;
import com.atlantbh.auctionappbackend.response.AppUserBidsResponse;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.atlantbh.auctionappbackend.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

    private final ProductRepository productRepository;

    private final AppUserRepository appUserRepository;

    private final RabbitTemplate rabbitTemplate;

    private final TokenService tokenService;

    public List<AppUserBidsResponse> getBidsForAppUser(HttpServletRequest request) {

        AppUser user = tokenService.getAuthenticatedUser(request);

        List<Bid> bids = bidRepository.findAllByUserId(user.getId(), Sort.by(Sort.Direction.DESC, BID_DATE));

        if (bids.isEmpty()) {
            return new ArrayList<>();
        }

        return bids.stream()
                .map(bid -> new AppUserBidsResponse(
                        bid.getId(),
                        bid.getPrice(),
                        SingleProductResponse.builder()
                                .id(bid.getProduct().getId())
                                .productName(bid.getProduct().getProductName())
                                .description(bid.getProduct().getDescription())
                                .startPrice(bid.getProduct().getStartPrice())
                                .images(bid.getProduct().getImages().stream().map(Image::getImageUrl).toList())
                                .endDate(bid.getProduct().getEndDate())
                                .numberOfBids(bid.getProduct().getNumberOfBids())
                                .highestBid(bid.getProduct().getHighestBid())
                                .sold(bid.getProduct().isSold())
                                .build(),
                        bid.getUser().getId()
                )).toList();
    }

    @Transactional(rollbackFor = {Exception.class, DataAccessException.class, AmqpException.class})
    public void createBidAndPublishToQueue(Long productId, float amount, HttpServletRequest request) throws ProductNotFoundException, AppUserNotFoundException {
        AppUser user = tokenService.getAuthenticatedUser(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        validateBidAmount(product, amount, user);

        PageRequest pageable = PageRequest.of(0, 1);
        List<UserMaxBidRecord> userWithHighestBid = bidRepository.findHighestBidAndUserByProduct(productId, pageable);

        Long highestBidUserId = null;

        if (!userWithHighestBid.isEmpty()) {
            highestBidUserId = userWithHighestBid.get(0).getUserId();
        }

        saveBidAndPublishNotification(user, product, amount, highestBidUserId);

    }

    private void saveBidAndPublishNotification(AppUser appUser, Product product, float amount, Long highestBidUserId) {
        Bid bid = Bid.builder().user(appUser).bidDate(ZonedDateTime.now()).product(product).price(amount).build();
        bidRepository.save(bid);

        if (highestBidUserId != null && !highestBidUserId.equals(appUser.getId())) {

            UUID uuid = UUID.randomUUID();

            NotificationResponse response = NotificationResponse.builder()
                    .id(uuid)
                    .date(ZonedDateTime.now())
                    .userId(highestBidUserId)
                    .productId(product.getId())
                    .description("You've been outbid on " + product.getProductName())
                    .type(NotificationType.OUTBID)
                    .build();

            rabbitTemplate.convertAndSend(OUTBID_EXCHANGE, OUTBID_ROUTING_KEY, response);
        }
    }


    private void validateBidAmount(Product product, float amount, AppUser appUser) {
        float currentMaxBid = product.getHighestBid();

        if (amount <= 0) {
            throw new BidAmountException("Bid can't be 0 or under that value");
        }

        if (currentMaxBid >= amount) {
            throw new BidAmountException("Place bid that is higher than the current one");
        }

        if (product.getStartDate().isAfter(ZonedDateTime.now())) {
            throw new AuctionStateException("Auction is yet to start for this product");
        }

        if (product.getEndDate().isBefore(ZonedDateTime.now())) {
            throw new AuctionStateException("Auction ended for this product");
        }

        if (product.isOwner(appUser.getId())) {
            throw new IllegalStateException("You can't bid on your own product");
        }
    }
}

