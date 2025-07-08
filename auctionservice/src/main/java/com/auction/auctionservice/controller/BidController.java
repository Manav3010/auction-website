package com.auction.auctionservice.controller;

import com.auction.auctionservice.model.BidRequestVO;
import com.auction.auctionservice.model.BidVO;
import com.auction.auctionservice.service.BidService;
import com.auction.auctionservice.constants.AuctionConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    @Qualifier(AuctionConstants.BIDSERVICEIMPL)
    private BidService bidService;

    private static final Logger logger = LogManager.getLogger(BidController.class);

    @PostMapping
    public ResponseEntity<String> placeBid(@RequestBody BidRequestVO request) {
        try {
            bidService.placeBid(request);
            return ResponseEntity.ok("Bid placed successfully.");
        } catch (Exception e) {
            logger.fatal("Error in placeBid: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place bid.");
        }
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidVO>> getBidsForAuction(@PathVariable String auctionId) {
        try {
            return ResponseEntity.ok(bidService.getBidsForAuction(auctionId));
        } catch (Exception e) {
            logger.fatal("Error in getBidsForAuction: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
