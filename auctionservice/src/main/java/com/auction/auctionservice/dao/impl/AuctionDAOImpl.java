package com.auction.auctionservice.dao.impl;

import com.auction.auctionservice.constants.AuctionConstants;
import com.auction.auctionservice.dao.AuctionDAO;
import com.auction.auctionservice.model.AuctionVO;
import com.auction.auctionservice.rowmapper.AuctionVORowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(AuctionConstants.AUCTIONDAOIMPL)
@PropertySource("classpath:postgres.properties")
public class AuctionDAOImpl implements AuctionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${CREATE_AUCTION}")
    private String createAuctionSQL;

    @Value("${FIND_AUCTION_BY_ID}")
    private String findAuctionByIdSQL;

    @Value("${FIND_AUCTIONS_BY_SELLER_ID}")
    private String findAuctionsBySellerIdSQL;

    @Value("${FIND_ALL_AUCTIONS}")
    private String findAllAuctionsSQL;

    private static final Logger logger = LogManager.getLogger(AuctionDAOImpl.class);

    @Override
    public void createAuction(AuctionVO auction) {
        try {
            jdbcTemplate.update(
                    createAuctionSQL,
                    auction.getAuctionId(),
                    auction.getItemName(),
                    auction.getDescription(),
                    auction.getStartingPrice(),
                    auction.getCurrentPrice(),
                    auction.getSellerId(),
                    auction.getStartTime(),
                    auction.getEndTime(),
                    auction.isActive()
            );
        } catch (Exception e) {
            logger.fatal("Error in createAuction: " + e);
            throw e;
        }
    }

    @Override
    public AuctionVO getAuctionById(String auctionId) {
        try {
            return jdbcTemplate.queryForObject(
                    findAuctionByIdSQL,
                    new Object[]{auctionId},
                    new AuctionVORowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            logger.fatal("Auction not found for ID: " + auctionId + " — " + e);
            throw e;
        } catch (Exception e) {
            logger.fatal("Error in getAuctionById: " + e);
            throw e;
        }
    }

    @Override
    public List<AuctionVO> getAuctionsBySellerId(String userId) {
        try {
            return jdbcTemplate.query(
                    findAuctionsBySellerIdSQL,
                    new Object[]{userId},
                    new AuctionVORowMapper()
            );
        } catch (Exception e) {
            logger.fatal("Error in getAuctionsBySellerId: " + e);
            throw e;
        }
    }

    @Override
    public List<AuctionVO> getAllAuctions() {
        try {
            return jdbcTemplate.query(
                    findAllAuctionsSQL,
                    new AuctionVORowMapper()
            );
        } catch (Exception e) {
            logger.fatal("Error in getAllAuctions: " + e);
            throw e;
        }
    }
}
