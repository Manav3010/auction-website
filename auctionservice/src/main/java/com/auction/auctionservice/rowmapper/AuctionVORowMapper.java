package com.auction.auctionservice.rowmapper;

import com.auction.auctionservice.model.AuctionVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionVORowMapper implements RowMapper<AuctionVO> {

    private static final Logger logger = LogManager.getLogger(AuctionVORowMapper.class);

    @Override
    public AuctionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuctionVO auction = new AuctionVO();
        try {
            auction.setAuctionId(rs.getString("auction_id"));
            auction.setItemName(rs.getString("item_name"));
            auction.setDescription(rs.getString("description"));
            auction.setStartingPrice(rs.getDouble("starting_price"));
            auction.setCurrentPrice(rs.getDouble("current_price"));
            auction.setSellerId(rs.getString("seller_id"));
            auction.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            auction.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            auction.setActive(rs.getBoolean("is_active"));

            if (rs.getTimestamp("created_at") != null)
                auction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            if (rs.getTimestamp("updated_at") != null)
                auction.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        } catch (Exception e) {
            logger.fatal("Error in mapRow (AuctionRowMapper): " + e);
            throw e;
        }
        return auction;
    }
}
