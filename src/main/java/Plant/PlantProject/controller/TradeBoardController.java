package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.dto.TradeBoardRequestDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.kakao.KaKaoService;
import Plant.PlantProject.service.MemberService;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static Plant.PlantProject.Entity.Status.판매중;
/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 컨트롤러 글 작성 구현
 특이 사항: 프론트 협업시 글작성 api url은 "/post"
*/
/**
 * packageName    : Plant/PlantProject/controller
 * fileName       : TradeBoardController
 * author         : 이민우
 * date           : 2023-02-24
 * description    : 거래 게시글 컨트롤러 글 작성 구현
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-02-23        이민우       최초 생성
 * 2022-02-24        이민우       게시글 페이징
 * 2022-02-24        이민우       특이사항 : boardContent를 엔티티 대신 dto 사용하고픔(추후 변경
 * 2022-02-28        이민우       특이사항 : boardContent를 엔티티 대신 dto 사용(테스트필수)
 * 2022-02-28        이민우       글 수정, 삭제기능 구현 (테스트 요망)
 * 2022-03-05        이민우       프론트 연동 실패(db에 값이 안들어감), @RestController로 바꿔서 json 데이터로 보내도 안돰
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeBoardController {
    private final TradeBoardService tradeBoardService;
    private final MemberService memberService;
    private final KaKaoService kaKaoService;
    @PostMapping("/write")
    public ResponseEntity<TradeDto> write(Principal principal, @RequestBody TradeBoardRequestDto tradeBoardDto) {

        UserDetails userDetails = (UserDetails) memberService.loadUserByUsername(principal.getName());
        System.out.println("userDetails = " + userDetails);
        Member member = memberService.getUser(userDetails.getUsername());
        tradeBoardDto.setCreateBy(userDetails.getUsername());
        tradeBoardDto.setMemberId(member.getId());
        TradeDto tradeDto = tradeBoardService.saveTradePost(tradeBoardDto);
        System.out.println("tradeBoardDto = " + tradeBoardDto.getId());
        return ResponseEntity.ok().body(tradeDto);
    }
// 글리스트 페이징
    @GetMapping("/write")
    public ResponseEntity<Page<TradeDto>> boardList(@RequestParam(required = false, defaultValue = "") String search, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Page<TradeDto> tradeBoardDtos = tradeBoardService.pageList(search, pageable);
        return ResponseEntity.ok(tradeBoardDtos);
    }
    //글 자세히보기
    @GetMapping("/list/{id}")
    public ResponseEntity<TradeDto> boardContent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tradeBoardService.findByIdx(id));
    }
    //글 수정
    @PutMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> update(Principal principal, @PathVariable("id") Long id, @RequestBody TradeBoardDto tradeBoardDto){
        UserDetails userDetails = (UserDetails) memberService.loadUserByUsername(principal.getName());
        System.out.println("userDetails = " + userDetails);
        Member member = memberService.getUser(userDetails.getUsername());
        tradeBoardDto.setCreateBy(userDetails.getUsername());
        tradeBoardDto.setMember(member);
        TradeBoardDto updatedTradeBoardDto=tradeBoardService.updateTradePost(tradeBoardDto);
        return ResponseEntity.ok(updatedTradeBoardDto);
    }
    //글 삭제
    @DeleteMapping("/list/{id}")
    public ResponseEntity<TradeBoardDto> delete(@PathVariable("id") Long id){
        TradeBoardDto tradeBoardDto= tradeBoardService.findById(id);
        System.out.println(tradeBoardDto.getId());
        tradeBoardService.deletePost(tradeBoardDto);
        return ResponseEntity.noContent().build();
    }
    //조회수 증가
    @GetMapping("/read/{id}")
    public ResponseEntity<Integer> read(@PathVariable Long id) {
        TradeBoardDto tradeBoardDto = tradeBoardService.findById(id);
        int view=tradeBoardService.updateView(id); // views ++

        return ResponseEntity.ok(view);
}
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<TradeBoardDto> updateStatus(@PathVariable Long id, @RequestBody TradeBoardDto tradeBoardDto) {
        TradeBoardDto updateStatus = tradeBoardService.updateStatus(tradeBoardDto);
        return ResponseEntity.ok().body(updateStatus);
    }

}