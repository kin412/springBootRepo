package com.kin.springbootproject1.batch.batchJob;

import org.springframework.batch.item.*;
import org.springframework.web.client.RestTemplate;

//동작 안하는 ExecutionContext 예시코드
// restTemplate 와 url 쓰는 것도 보셈
public class CustomItemStreamReaderImpl implements ItemStreamReader<String> {

    private final RestTemplate restTemplate;
    private int currentId;
    private final String CURRENT_ID_KEY = "current.call.id";
    private final String API_URL = "https://www.devyummi.com/page?id=";

    public CustomItemStreamReaderImpl(RestTemplate restTemplate) {

        this.currentId = 0;
        this.restTemplate = restTemplate;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        if (executionContext.containsKey(CURRENT_ID_KEY)) {
            currentId = executionContext.getInt(CURRENT_ID_KEY);
        }
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        currentId++;

        String url = API_URL + currentId;
        String response = restTemplate.getForObject(url, String.class);

        if (response == null) {
            return null;
        }
        return response;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt(CURRENT_ID_KEY, currentId);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
