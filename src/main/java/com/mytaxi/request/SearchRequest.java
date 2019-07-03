package com.mytaxi.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.domainvalue.OnlineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest
{
    private SearchDriverRequest searchDriverRequest;

    private SearchCarRequest searchCarRequest;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchDriverRequest
    {
        private String userName;

        private OnlineStatus onlineStatus;

        public boolean isEmpty() {
            return (this.userName == null && this.onlineStatus == null);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class SearchCarRequest
    {
        private String licensePlate;

        private Boolean selected;

        private Integer seatCount;

        private Integer rating;

        public boolean isEmpty() {
            return (this.licensePlate == null && this.selected == null
                && this.seatCount == null && this.rating == null);
        }
    }
}
