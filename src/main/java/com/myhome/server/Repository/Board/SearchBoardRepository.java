package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Board;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class SearchBoardRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Board> findSearchTitle(List<String> lists){
        StringBuilder query = new StringBuilder("SELECT b FROM Board b WHERE");
        for (int i = 0 ; i < lists.size() ; i++) {
            if(i == (lists.size()-1)){
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%'");
            }else{
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%' or");
            }

        }
        return em.createQuery(query.toString(), Board.class)
                .getResultList();
    }
    public List<Board> findSearchTitle(List<String> lists, PageRequest pageRequest){
        StringBuilder query = new StringBuilder("SELECT b FROM Board b WHERE");
        for (int i = 0 ; i < lists.size() ; i++) {
            if(i == (lists.size()-1)){
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%'");
            }else{
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%' or");
            }

        }
        if(pageRequest.getSort().isSorted()){
            query.append(" ORDER BY ").append(
                    pageRequest.getSort().toString().replace(":",""));
        }
        TypedQuery<Board> query1 = em.createQuery(query.toString(), Board.class);
        query1.setFirstResult((int) pageRequest.getOffset());
        query1.setMaxResults(pageRequest.getPageSize());
        return query1.getResultList();

    }

    public List<Board> findByKeyword(List<String> keywords){
        StringBuilder query = new StringBuilder("SELECT b FROM Board b WHERE ");
        for (int i = 0 ; i < keywords.size() ; i++) {
            if(i == (keywords.size()-1)){
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.keywords LIKE '%").append(keywords.get(i)).append("%'");
            }else{
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.keywords LIKE '%").append(keywords.get(i)).append("%' or");
            }

        }
        return em.createQuery(query.toString(), Board.class).getResultList();
    }
    public List<Board> findByKeyword(List<String> keywords, PageRequest pageRequest){
        StringBuilder query = new StringBuilder("SELECT b FROM Board b WHERE ");
        for (int i = 0 ; i < keywords.size() ; i++) {
            if(i == (keywords.size()-1)){
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.keywords LIKE '%").append(keywords.get(i)).append("%'");
            }else{
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.keywords LIKE '%").append(keywords.get(i)).append("%' or");
            }

        }
        if(pageRequest.getSort().isSorted()){
            query.append(" ORDER BY ").append(
                    pageRequest.getSort().toString().replace(":",""));
        }
        TypedQuery<Board> query1 = em.createQuery(query.toString(), Board.class);
        query1.setFirstResult((int) pageRequest.getOffset());
        query1.setMaxResults(pageRequest.getPageSize());
        return query1.getResultList();
    }
}
