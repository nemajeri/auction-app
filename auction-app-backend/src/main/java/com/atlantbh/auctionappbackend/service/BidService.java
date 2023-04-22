package com.atlantbh.auctionappbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
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
    public void createBid(Long productId, float amount, HttpServletRequest request) throws ProductNotFoundException {
        AppUser appUser = getAppUserFromRequest(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        validateBidAmount(product, amount);

        Bid bid = new Bid();
        bid.setUser(appUser);
        bid.setProduct(product);
        bid.setPrice(amount);
        bidRepository.save(bid);
    }

    private AppUser getAppUserFromRequest(HttpServletRequest request) {
        String jwt = tokenService.getJwtFromHeader(request);

        if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
            String email = tokenService.getClaimFromToken(jwt, "email");
            return appUserRepository.getByEmail(email)
                    .orElseThrow(() -> new AppUserNotFoundException("User with email " + email + " not found"));
        }
        throw new BadCredentialsException("Invalid JWT token");
    }

    private void validateBidAmount(Product product, float amount) {
        float currentMaxBid = product.getCurrentMaxBid();

        if (amount <= 0) {
            throw new IllegalArgumentException("Bid can't be 0 or under that value");
        }

        if (currentMaxBid >= amount) {
            throw new IllegalArgumentException("Place bid that is higher than the current one");
        }
    }
}

