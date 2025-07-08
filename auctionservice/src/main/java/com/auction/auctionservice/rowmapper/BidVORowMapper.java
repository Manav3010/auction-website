package com.auction.auctionservice.rowmapper;

import com.auction.auctionservice.model.BidVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BidVORowMapper implements RowMapper<BidVO> {

    private static final Logger logger = LogManager.getLogger(BidVORowMapper.class);

    @Override
    public BidVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        BidVO bid = new BidVO();
        try {
            bid.setBidId(rs.getString("bid_id"));
            bid.setAmount(rs.getDouble("amount"));
            bid.setAuctionId(rs.getString("auction_id"));
            bid.setBidderId(rs.getString("bidder_id"));
            bid.setBidTime(rs.getTimestamp("bid_time").toLocalDateTime());
        } catch (Exception e) {
            logger.fatal("Error in BidVORowMapper mapRow: " + e.getMessage(), e);
            throw e;
        }
        return bid;
    }
}
