package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.exception.BadRequestException;
import com.atlantbh.auctionappbackend.exception.BidAmountException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.*;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.request.UserMaxBidRequest;
import com.atlantbh.auctionappbackend.response.AppUserBidsResponse;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.atlantbh.auctionappbackend.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

    private final ProductRepository productRepository;

    private final AppUserRepository appUserRepository;

    private final RabbitTemplate rabbitTemplate;

    public List<AppUserBidsResponse> getBidsForAppUser(Long userId) {
        List<Bid> bids = bidRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, BID_DATE));

        if (bids.isEmpty()) {
            bids = new ArrayList<>();
        }

        return bids.stream()
                .map(bid -> new AppUserBidsResponse(
                        bid.getId(),
                        bid.getPrice(),
                        new SingleProductResponse(
                                bid.getProduct().getId(),
                                bid.getProduct().getProductName(),
                                bid.getProduct().getDescription(),
                                bid.getProduct().getStartPrice(),
                                bid.getProduct().getImages().stream().map(Image::getImageUrl).toList(),
                                bid.getProduct().getEndDate(),
                                bid.getProduct().getNumberOfBids(),
                                bid.getProduct().getHighestBid(),
                                bid.getProduct().isSold(),
                                bid.getProduct().getUser().getId()
                        ),
                        bid.getUser().getId()
                ))
                .toList();
    }

    @Transactional
    public void createBid(Long productId, float amount, Long userId) throws ProductNotFoundException, AppUserNotFoundException {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new AppUserNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        validateBidAmount(product, amount, appUser);

        PageRequest pageable = PageRequest.of(0, 1);
        List<UserMaxBidRequest> userWithHighestBid = bidRepository.findHighestBidAndUserByProduct(productId, pageable);

        Long highestBidUserId = null;

        if (!userWithHighestBid.isEmpty()) {
            highestBidUserId = userWithHighestBid.get(0).getUserId();
        }
        try {

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
        } catch (DataAccessException | AmqpException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
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
            throw new BadRequestException("Auction is yet to start for this product");
        }

        if (product.getEndDate().isBefore(ZonedDateTime.now())) {
            throw new BadRequestException("Auction ended for this product");
        }

        if (product.isOwner(appUser.getId())) {
            throw new BadRequestException("You can't bid on your own product");
        }
    }
}

