package com.example.firstproject.api;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {

    //의존성주입
    @Autowired
    private CommentService commentService;

    // 1. 댓글 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {
        // 서비스에게 작업 위임
        List<CommentDto> dtos = commentService.comments(articleId);

        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }


    //2. 댓글 생성
    //1.메서드 생성
    @PostMapping("api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto>
        create(@PathVariable Long articleId,
               @RequestBody CommentDto dto){

        //2.(댓글 생성)서비스에 위임
        CommentDto createDto
                = commentService.create(articleId,dto);

        //결과 응답-> 댓글 생성이 성공했을 때 응답
        return ResponseEntity.status(HttpStatus.OK)
                .body(createDto);

        //결과 응답-> 댓글 생성이 실패했을 때?
        //*예외처리: 스프링부트가 담당
    }

    //댓글 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto>
    update(@PathVariable Long id,
           @RequestBody CommentDto dto){
        //서비스 위임
        CommentDto updateDto = commentService.update(id,dto);
        //결과 반환(응답)
        return ResponseEntity.status(HttpStatus.OK).body(updateDto);
    }

    //댓글 삭제
    @DeleteMapping("api/comments/{id}")
    public ResponseEntity<CommentDto>
    delete(@PathVariable Long id)  {   //댓글 삭제 요청 접수

        //서비스 위임
        CommentDto deleteDto = CommentService.delete(id);

        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(deleteDto);
    }
}
