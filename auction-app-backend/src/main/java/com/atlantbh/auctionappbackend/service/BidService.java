package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.atlantbh.auctionappbackend.exception.BadRequestException;
import com.atlantbh.auctionappbackend.model.Notification;
import com.atlantbh.auctionappbackend.repository.NotificationRepository;
import com.atlantbh.auctionappbackend.request.UserMaxBidRequest;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.exception.BidAmountException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.atlantbh.auctionappbackend.utils.Constants.BID_DATE;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

    private final ProductRepository productRepository;

    private final AppUserRepository appUserRepository;

    private final SimpMessagingTemplate template;

    private final NotificationRepository notificationRepository;

    public List<Bid> getBidsForAppUser(Long userId) {
        List<Bid> bids = bidRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, BID_DATE));
        if (bids.isEmpty()) {
            bids = new ArrayList<>();
        }
        return bids;
    }

    @Transactional
    public void createBid(Long productId, float amount) throws ProductNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        AppUser appUser = appUserRepository.getByEmail(email)
                .orElseThrow(() -> new AppUserNotFoundException("User with email " + email + " not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        validateBidAmount(product, amount, appUser);

        Optional<UserMaxBidRequest> userMaxBidRequest = bidRepository.findFirstByProductAndUserOrderByPriceDesc(productId, appUser.getId());

        UserMaxBidRequest bidDetails = userMaxBidRequest.orElseThrow(() -> new AppUserNotFoundException("Invalid user"));

        Bid bid = new Bid();
        bid.setUser(appUser);
        bid.setProduct(product);
        bid.setPrice(amount);
        bidRepository.save(bid);

        Notification notification = new Notification();
        notification.setUser(appUserRepository.getById(bidDetails.getId()));
        notification.setProduct(product);
        notification.setType(NotificationType.OUTBIDDED);
        template.convertAndSendToUser(notification.getUser().getEmail(), "/topic/notifications", notification);
        notificationRepository.save(notification);
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

        if (product.isOwner(appUser.getEmail())) {
            throw new BadRequestException("You can't bid on your own product");
        }
    }
}

