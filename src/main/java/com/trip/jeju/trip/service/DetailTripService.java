package com.trip.jeju.trip.service;

import com.trip.jeju.trip.vo.DetailAllResVO;
import com.trip.jeju.trip.vo.DetailCommonResVO;
import com.trip.jeju.trip.vo.DetailImageResVO;
import com.trip.jeju.trip.vo.DetailInfoResVO;
import com.trip.jeju.trip.vo.DetailIntroResVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailTripService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ks.key}")
    private String key;

    @Value("${ks.url}")
    private String baseUrl;

    @Value("${api.mobile.os}")
    private String os;

    @Value("${api.mobile.app}")
    private String app;

    public DetailCommonResVO detailCommon(Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/detailCommon2")
                .queryParam("serviceKey", key)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("numOfRows", 1)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json");

        params.forEach(builder::queryParam);

        String url = builder.build(false).toUriString();

        log.info("공통정보조회 URL = {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("공통정보조회 응답 JSON = {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root.path("response").path("header").path("resultCode").asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("공통정보조회 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return null;
            }

            JsonNode item = root.path("response").path("body").path("items").path("item").get(0);

            if (item == null || item.isMissingNode()) {
                log.warn("공통정보조회 결과 없음");
                return null;
            }

            return DetailCommonResVO.builder()
                    .contentid(item.path("contentid").asText())
                    .title(item.path("title").asText())
                    .overview(item.path("overview").asText())
                    .homepage(item.path("homepage").asText())
                    .tel(item.path("tel").asText())
                    .addr1(item.path("addr1").asText())
                    .firstimage(item.path("firstimage").asText())
                    .build();

        } catch (Exception e) {
            log.error("공통정보조회 API 호출 실패 - {}", e.getMessage());
            return null;
        }
    }

    public DetailIntroResVO detailIntro(Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/detailIntro2")
                .queryParam("serviceKey", key)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("numOfRows", 1)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json");

        params.forEach(builder::queryParam);

        String url = builder.build(false).toUriString();

        log.info("소개정보조회 URL = {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("소개정보조회 응답 JSON = {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root.path("response").path("header").path("resultCode").asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("소개정보조회 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return null;
            }

            JsonNode item = root.path("response").path("body").path("items").path("item").get(0);

            if (item == null || item.isMissingNode()) {
                log.warn("소개정보조회 결과 없음");
                return null;
            }

            return DetailIntroResVO.builder()
                    .contentid(item.path("contentid").asText())
                    .contenttypeid(item.path("contenttypeid").asText())
                    .infocenter(item.path("infocenter").asText())
                    .restdate(item.path("restdate").asText())
                    .usetime(item.path("usetime").asText())
                    .parking(item.path("parking").asText())
                    .chkpet(item.path("chkpet").asText())
                    .expguide(item.path("expguide").asText())
                    .expagerange(item.path("expagerange").asText())
                    .infocentershopping(item.path("infocentershopping").asText())
                    .restdateshopping(item.path("restdateshopping").asText())
                    .parkingshopping(item.path("parkingshopping").asText())
                    .opentime(item.path("opentime").asText())
                    .restroom(item.path("restroom").asText())
                    .saleitem(item.path("saleitem").asText())
                    .build();

        } catch (Exception e) {
            log.error("소개정보조회 API 호출 실패 - {}", e.getMessage());
            return null;
        }
    }

    public List<DetailInfoResVO> detailInfo(Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/detailInfo2")
                .queryParam("serviceKey", key)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json");

        params.forEach(builder::queryParam);

        String url = builder.build(false).toUriString();

        log.info("반복정보조회 URL = {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("반복정보조회 응답 JSON = {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root.path("response").path("header").path("resultCode").asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("반복정보조회 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return new ArrayList<>();
            }

            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<DetailInfoResVO> list = new ArrayList<>();

            if (items == null || items.isMissingNode()) {
                log.warn("반복정보조회 결과 없음");
                return list;
            }

            if (items.isArray()) {
                for (JsonNode item : items) {
                    list.add(toDetailInfoResVO(item));
                }
            } else {
                list.add(toDetailInfoResVO(items));
            }

            return list;

        } catch (Exception e) {
            log.error("반복정보조회 API 호출 실패 - {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<DetailImageResVO> detailImage(Map<String, Object> params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/detailImage2")
                .queryParam("serviceKey", key)
                .queryParam("MobileOS", os)
                .queryParam("MobileApp", app)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json")
                .queryParam("imageYN", "Y");

        builder.queryParam("contentId", params.get("contentId"));

        String url = builder.build(false).toUriString();

        log.info("이미지정보조회 URL = {}", url);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            log.debug("이미지정보조회 응답 JSON = {}", jsonResponse);

            JsonNode root = objectMapper.readTree(jsonResponse);

            String resultCode = root.path("response").path("header").path("resultCode").asText();

            if (!"0000".equals(resultCode)) {
                String resultMsg = root.path("response").path("header").path("resultMsg").asText();
                log.warn("이미지정보조회 API 오류 - resultCode: {}, resultMsg: {}", resultCode, resultMsg);
                return new ArrayList<>();
            }

            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<DetailImageResVO> list = new ArrayList<>();

            if (items == null || items.isMissingNode()) {
                log.warn("이미지정보조회 결과 없음");
                return list;
            }

            if (items.isArray()) {
                for (JsonNode item : items) {
                    list.add(toDetailImageResVO(item));
                }
            } else {
                list.add(toDetailImageResVO(items));
            }

            return list;

        } catch (Exception e) {
            log.error("이미지정보조회 API 호출 실패 - {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public DetailAllResVO detailAll(Map<String, Object> params) {

        Map<String, Object> commonParams = new HashMap<>();
        commonParams.put("contentId", params.get("contentId"));

        Map<String, Object> introParams = new HashMap<>();
        introParams.put("contentId", params.get("contentId"));
        introParams.put("contentTypeId", params.get("contentTypeId"));

        Map<String, Object> infoParams = new HashMap<>();
        infoParams.put("contentId", params.get("contentId"));
        infoParams.put("contentTypeId", params.get("contentTypeId"));

        Map<String, Object> imageParams = new HashMap<>();
        imageParams.put("contentId", params.get("contentId"));

        return DetailAllResVO.builder()
                .common(detailCommon(commonParams))
                .intro(detailIntro(introParams))
                .info(detailInfo(infoParams))
                .image(detailImage(imageParams))
                .build();
    }

    private DetailInfoResVO toDetailInfoResVO(JsonNode item) {
        return DetailInfoResVO.builder()
                .contentid(item.path("contentid").asText())
                .contenttypeid(item.path("contenttypeid").asText())
                .fldgubun(item.path("fldgubun").asText())
                .infoname(item.path("infoname").asText())
                .infotext(item.path("infotext").asText())
                .serialnum(item.path("serialnum").asText())
                .subcontentid(item.path("subcontentid").asText())
                .subdetailalt(item.path("subdetailalt").asText())
                .subdetailimg(item.path("subdetailimg").asText())
                .subdetailoverview(item.path("subdetailoverview").asText())
                .subname(item.path("subname").asText())
                .subnum(item.path("subnum").asText())
                .roomcode(item.path("roomcode").asText())
                .roomtitle(item.path("roomtitle").asText())
                .roomsize1(item.path("roomsize1").asText())
                .roomcount(item.path("roomcount").asText())
                .roombasecount(item.path("roombasecount").asText())
                .roommaxcount(item.path("roommaxcount").asText())
                .roomoffseasonminfee1(item.path("roomoffseasonminfee1").asText())
                .roomoffseasonminfee2(item.path("roomoffseasonminfee2").asText())
                .roompeakseasonminfee1(item.path("roompeakseasonminfee1").asText())
                .roompeakseasonminfee2(item.path("roompeakseasonminfee2").asText())
                .roomintro(item.path("roomintro").asText())
                .roombathfacility(item.path("roombathfacility").asText())
                .roombath(item.path("roombath").asText())
                .roomhometheater(item.path("roomhometheater").asText())
                .roomaircondition(item.path("roomaircondition").asText())
                .roomtv(item.path("roomtv").asText())
                .roompc(item.path("roompc").asText())
                .roomcable(item.path("roomcable").asText())
                .roominternet(item.path("roominternet").asText())
                .roomrefrigerator(item.path("roomrefrigerator").asText())
                .roomtoiletries(item.path("roomtoiletries").asText())
                .roomsofa(item.path("roomsofa").asText())
                .roomcook(item.path("roomcook").asText())
                .roomtable(item.path("roomtable").asText())
                .roomhairdryer(item.path("roomhairdryer").asText())
                .roomsize2(item.path("roomsize2").asText())
                .roomimg1(item.path("roomimg1").asText())
                .roomimg1alt(item.path("roomimg1alt").asText())
                .roomimg2(item.path("roomimg2").asText())
                .roomimg2alt(item.path("roomimg2alt").asText())
                .roomimg3(item.path("roomimg3").asText())
                .roomimg3alt(item.path("roomimg3alt").asText())
                .roomimg4(item.path("roomimg4").asText())
                .roomimg4alt(item.path("roomimg4alt").asText())
                .roomimg5(item.path("roomimg5").asText())
                .roomimg5alt(item.path("roomimg5alt").asText())
                .cpyrhtDivCd1(item.path("cpyrhtDivCd1").asText())
                .cpyrhtDivCd2(item.path("cpyrhtDivCd2").asText())
                .cpyrhtDivCd3(item.path("cpyrhtDivCd3").asText())
                .cpyrhtDivCd4(item.path("cpyrhtDivCd4").asText())
                .cpyrhtDivCd5(item.path("cpyrhtDivCd5").asText())
                .build();
    }

    private DetailImageResVO toDetailImageResVO(JsonNode item) {
        return DetailImageResVO.builder()
                .cpyrhtDivCd(item.path("cpyrhtDivCd").asText())
                .contentid(item.path("contentid").asText())
                .imgname(item.path("imgname").asText())
                .originimgurl(item.path("originimgurl").asText())
                .serialnum(item.path("serialnum").asText())
                .smallimageurl(item.path("smallimageurl").asText())
                .build();
    }
}