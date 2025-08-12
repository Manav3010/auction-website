package com.auction.auctionservice.controller;

import com.auction.auctionservice.dto.AuctionRequestVO;
import com.auction.auctionservice.model.AuctionResponseVO;
import com.auction.auctionservice.service.AuctionService;
import com.auction.auctionservice.constants.AuctionConstants;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    @Qualifier(AuctionConstants.AUCTIONSERVICEIMPL)
    private AuctionService auctionService;

    private static final Logger logger = LogManager.getLogger(AuctionController.class);

    @PostMapping
    public ResponseEntity<AuctionResponseVO> createAuction(@Valid @RequestBody AuctionRequestVO request) {
        try {
            return ResponseEntity.ok(auctionService.createAuction(request));
        } catch (Exception e) {
            logger.fatal("Error in createAuction: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponseVO> getAuctionById(@PathVariable String auctionId) {
        try {
            return ResponseEntity.ok(auctionService.getAuctionById(auctionId));
        } catch (Exception e) {
            logger.fatal("Error in getAuctionById: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuctionResponseVO>> getAuctionsBySellerId(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(auctionService.getAuctionsBySellerId(userId));
        } catch (Exception e) {
            logger.fatal("Error in getAuctionsBySellerId: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AuctionResponseVO>> getAllAuctions() {
        try {
            return ResponseEntity.ok(auctionService.getAllAuctions());
        } catch (Exception e) {
            logger.fatal("Error in getAllAuctions: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // NEW: Update auction endpoint for frontend integration
    @PutMapping("/{auctionId}")
    public ResponseEntity<AuctionResponseVO> updateAuction(@PathVariable String auctionId, @Valid @RequestBody AuctionRequestVO request) {
        try {
            AuctionResponseVO updatedAuction = auctionService.updateAuction(auctionId, request);
            return ResponseEntity.ok(updatedAuction);
        } catch (Exception e) {
            logger.fatal("Error in updateAuction: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // NEW: Delete auction endpoint for frontend integration
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable String auctionId) {
        try {
            auctionService.deleteAuction(auctionId);
            return ResponseEntity.ok("Auction deleted successfully");
        } catch (Exception e) {
            logger.fatal("Error in deleteAuction: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete auction");
        }
    }
}
