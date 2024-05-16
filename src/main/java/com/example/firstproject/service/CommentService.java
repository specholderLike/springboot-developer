package com.example.firstproject.service;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentService {
    @Autowired
    private static CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;




    //■1.댓글 조회 메서드 -comments()
    public static List<CommentDto> comments(Long articleId) {
        /*// 1. 댓글 조회
        List<Comment> comments = commentRepository.findByArticleId(articleId);

        // 2. 엔티티 -> DTO 변환
        List<CommentDto> dtos = new ArrayList<CommentDto>();
        for (int i = 0; i < comments.size(); i++) {
            Comment c = comments.get(i);
            CommentDto dto = CommentDto.createCommentDto(c);
            dtos.add(dto);
        }*/


        // 3. 결과 반환
        //1.댓글 엔티티 목록 조회
        return commentRepository.findByArticleId(articleId)
                //2.댓글 엔티티 목록을 스트림으로 변환
                .stream()

                //3.엔티티를 DTO로 매핑
                .map(comment -> CommentDto
                        .createCommentDto(comment))

                //4.스트림을 리스트로 변환
                .collect(Collectors.toList());
    }



  //■1.댓글 생성 메서드 -create()
    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {
        //1.부모 게시글 가져오기
        Article article
                = articleRepository
                .findById(articleId)
                .orElseThrow(()->
                        new IllegalArgumentException
                                ("댓글 생성 실패!"
                                        +
                                "대상 게시글이 없습니다."));

        //2.댓글 엔티티 생성
        Comment comment = Comment.createComment(dto,article);

        //3.댓글 엔티티 DB에 저장
        Comment created =commentRepository.save(comment);

        //4.DTO로 변환 / 반환
        return CommentDto.createCommentDto(created);
    }

    //댓글 수정 메서드-update()
    public CommentDto update(Long id, CommentDto dto) {
        //댓글 조회-수정할 댓글 가져오기
        Comment target = commentRepository.findById(id)
                //예외처리 - 없으면 에러메세지 출력
                .orElseThrow(()-> new IllegalArgumentException("댓글 수정 실패!" + "대상 댓글이 없습니다."));

        //예외가 발생하지 않았다는 가정하에, 코드작성했음
        //*기존 댓글 엔티티에 수정
        target.patch(dto);

        //DB 갱신
        Comment updated = commentRepository.save(target);

        //댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);

    }
    
    @Transactional
    public static CommentDto delete(Long id) {
        // 댓글 조회 및 예외 발생

        // 삭제할 댓글 가져오기
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패!" + "대상이 없습니다"));

        // 댓글 삭제(예외 발생하지 않는다는 가정)
        commentRepository.delete(target);

        // 삭제 댓글 DTO 변환 및 반환
        return CommentDto.createCommentDto(target);
    }
}
