package com.kin.springbootproject1.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class restController {

    // hello world
    @GetMapping("/getText")
    public String getText() {
        return "hello world";
    }

    //객체를 담게 되면 application/json 형식으로 반환하게 됨
    /*
     * produces - 보내는 데이터에 대한 타입 (생략 가능)
     * consumes - 받는 데이터에 대한 타입
     */
    /*
        {
          "number": 123546,
          "name": "홍길동",
          "id": "1"
        }
     */
    @GetMapping(value="/getObject", produces = "application/json")
    public simpleVo getObject() {

        simpleVo vo = new simpleVo(123546, "홍길동", "1");

        return vo ;
    }

    //map형식의 반환 (map에 리스트도 담을 수 있음)
    /*
        {
          "total": 100,
          "data": {
            "number": 999,
            "name": "자르반",
            "id": "3"
          }
        }
     */
    @GetMapping("/getObject2")
    public Map<String, Object> getObject2(){

        Map<String, Object> map = new HashMap<>();
        simpleVo vo = new simpleVo(999, "자르반", "3");

        map.put("total", 100);
        map.put("data", vo);

        return map;

    }

    //list형식의 반환
    /*
        [
          {
            "number": 999,
            "name": "자르반",
            "id": "3"
          },
          {
            "number": 222,
            "name": "자르반3세",
            "id": "4"
          }
        ]
     */
    @GetMapping("/getObject3")
    public List<simpleVo> getObject3(){

        List<simpleVo> svList = new ArrayList<simpleVo>();
        simpleVo vo = new simpleVo(999, "자르반", "3");
        simpleVo vo2 = new simpleVo(222, "자르반3세", "4");

        svList.add(vo);
        svList.add(vo2);

        return svList;

    }

    //get형식 값을 받는 방법1 - 쿼리스트링 ? 키=값
    //http://localhost:8383/getKey?id=aaaa&name=홍길자
    @GetMapping("/getKey")
    public String getKey(@RequestParam("id") String id,
                         @RequestParam("name") String name) {

        System.out.println(id);
        System.out.println(name);

        return "success";
    }

    //get형식 값을 받는 방법2 - 쿼리파람 URL/키/키
    @GetMapping("/getPath/{sort}/{apiKey}")
    public String getPath(@PathVariable("sort") String sort,
                          @PathVariable("apiKey") String key) {

        System.out.println(sort);
        System.out.println(key);

        return "success";
    }

    //값을 받는 방법1 - VO로 맵핑
    //json형식의 데이터를 자바의 객체로 맵핑 -> @RequestBody

    @PostMapping("/getJson")
    public String getJson(@RequestBody simpleVo vo) {

        System.out.println("-- getJson : " + vo.toString());
        return "success";
    }

    //form 방식은 @RequestBody를 쓰지 않는다.
    /*
    @PostMapping("/getJson")
    public String getJson(simpleVo vo) {

        System.out.println(vo.toString());
        return "success";
    }
    */

    //map 방식
    @PostMapping("/getMap")
    public String getMap(@RequestBody Map<String, Object> map) {

        System.out.println(map.toString());
        return "success";
    }

    //consumer를 통한 데이터제한
    //consumer는 특정타입의 데이터를 받도록 처리하는 옵션(기본값 json)
    //클라이언트에는 Content-type을 이용해서 보내는 데이터에 대한 타입을 명시 (무조건 명시해야 함)
    @PostMapping(value="/getResult", consumes = "text/plain")
    public String getResult(@RequestBody String data) {

        System.out.println(data);

        return "success";
    }

    @PostMapping("/simpleVoApi")
    public simpleVo simpleVoApi(@RequestBody simpleVo vo) {
        log.info("-- before simpleVoApi : " + vo.toString());
        vo.setName("장독대");
        return vo;
    }

    //UriComponentsBuilder
    @GetMapping("/restTemplate")
    public String getRestTemplate() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path("/getObject3")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        log.info("--responseEntity.getStatusCode() : " + responseEntity.getStatusCode().toString());
        log.info("--responseEntity.getBody()" + responseEntity.getBody());

        return responseEntity.getBody();
    }

    //addHeader
    @GetMapping("/restTemplate2")
    public ResponseEntity<simpleVo> addHeader() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path("/simpleVoApi")
                //.queryParam()
                .encode()
                .build()
                //.expand()
                .toUri();

        simpleVo vo = new simpleVo();
        vo.setId("1");
        vo.setName("김득춘");
        vo.setNumber(999);

        RequestEntity<simpleVo> requestEntity = RequestEntity
                .post(uri)
                .header("testHeaderName", "testHeaderValue")
                .body(vo);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<simpleVo> responseEntity = restTemplate.exchange(requestEntity, simpleVo.class);

        log.info("--restTemplate2 responseEntity.getStatusCode() : " + responseEntity.getStatusCode().toString());
        log.info("--restTemplate2 responseEntity.getBody()" + responseEntity.getBody().toString());

        return responseEntity;
    }

}
