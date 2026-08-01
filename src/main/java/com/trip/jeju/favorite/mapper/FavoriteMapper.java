package com.trip.jeju.favorite.mapper;

import com.trip.jeju.favorite.vo.FavoriteSearchReq;
import com.trip.jeju.favorite.vo.FavoriteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    void createFavorite(FavoriteVO reqVO);
    List<FavoriteVO> selectFavoriteList(FavoriteSearchReq reqVO);
    int selectFavoriteCount(FavoriteSearchReq reqVO);
    void deleteFavorite(@Param("favoriteId") Integer favoriteId);
    FavoriteVO selectFavoriteById(@Param("favoriteId") Integer favoriteId, @Param("userId") Integer userId);
    List<FavoriteVO> selectMainFavoriteList(Integer userId);
}
