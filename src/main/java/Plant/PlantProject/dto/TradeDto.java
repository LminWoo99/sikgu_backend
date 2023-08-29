package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Comment;
import Plant.PlantProject.Entity.DeleteStatus;
import Plant.PlantProject.Entity.TradeBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeDto {
    private Long id;
    private String title;
    private String content;
    private String createBy;
    private Long memberId;
    private int view;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int price;
    public static TradeDto convertTradeBoardToDto(TradeBoard tradeBoard) {
        System.out.println("tradeBoard dto = " + tradeBoard.getId());
        return new TradeDto(tradeBoard.getId(), tradeBoard.getTitle(),tradeBoard.getContent(),tradeBoard.getCreateBy(),
                tradeBoard.getMember().getId(),  tradeBoard.getView(),tradeBoard.getStatus().name(),
                tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getPrice());

    }

}
