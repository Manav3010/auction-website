package com.auction.schedularservice.model;




import java.time.LocalDateTime;



public class AuctionVO {


    private String auctionId;
    private long endTime;


    public AuctionVO(long endTime, String auctionId) {
        this.endTime = endTime;
        this.auctionId = auctionId;
    }
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
