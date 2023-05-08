package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.BadRequestException;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

    private final ProductRepository productRepository;

    private final AppUserRepository appUserRepository;

    private final TokenService tokenService;

    public List<Bid> getBidsForAppUser(Long userId) {
        List<Bid> bids = bidRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "bidDate"));
        if (bids.isEmpty()) {
            throw new AppUserNotFoundException("Bids from user with id: " + userId + " not found");
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

        Bid bid = new Bid();
        bid.setUser(appUser);
        bid.setProduct(product);
        bid.setPrice(amount);
        bidRepository.save(bid);
    }


    private void validateBidAmount(Product product, float amount, AppUser appUser) {
        float currentMaxBid = product.getCurrentMaxBid();

        if (amount <= 0) {
            throw new BidAmountException("Bid can't be 0 or under that value");
        }

        if (currentMaxBid >= amount) {
            throw new BidAmountException("Place bid that is higher than the current one");
        }

        if (product.getStartDate().isAfter(LocalDateTime.now(ZoneOffset.UTC).plusHours(2))) {
            throw new BadRequestException("Auction is yet to start for this product");
        }

        if (product.getEndDate().isBefore(LocalDateTime.now(ZoneOffset.UTC).plusHours(2))) {
            throw new BadRequestException("Auction ended for this product");
        }

        if (product.isOwner(appUser.getEmail())) {
            throw new BadRequestException("You can't bid on your own product");
        }

        Optional<Float> usersMaxBidForProductOpt = bidRepository.getMaxBidFromUserForProduct(appUser.getId(), product.getId());
        if (usersMaxBidForProductOpt.isPresent() && usersMaxBidForProductOpt.get() >= 0) {
            float usersMaxBidForProduct = usersMaxBidForProductOpt.get();
            if (amount <= usersMaxBidForProduct) {
                throw new BadRequestException("Price can't be lower than your previous bid of $" + usersMaxBidForProduct);
            }
        }
    }
}

