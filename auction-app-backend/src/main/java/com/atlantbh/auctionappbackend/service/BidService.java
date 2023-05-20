package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.BadRequestException;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    public List<Bid> getBidsForAppUser(Long userId) {
        List<Bid> bids = bidRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, BID_DATE));
        if (bids.isEmpty()) {
            bids = new ArrayList<>();
        }
        return bids;
    }

    public Float getHighestBidder(Long userId, Long productId) {
        Optional<Float> highestBidOpt = bidRepository.getMaxBidFromUserForProduct(userId, productId);
        return highestBidOpt.orElse(0f);
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

        Bid bid = new Bid();
        bid.setUser(appUser);
        bid.setProduct(product);
        bid.setPrice(amount);
        bidRepository.save(bid);
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

