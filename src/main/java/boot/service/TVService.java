package boot.service;

import boot.dto.ScheduleDTO;
import boot.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TVService {

    private AsyncRestTemplate restTemplate = new AsyncRestTemplate();

    public DeferredResult<ScheduleDTO> nextOnAir(String showTitle) {

        final DeferredResult<ScheduleDTO> result = new DeferredResult<>();

        final DeferredResult<List<ScheduleDTO>> svt1 = getScheduledPrograms("svt1");

        svt1.setResultHandler(svt1Result -> {

            ((List<ScheduleDTO>) svt1Result).stream()
                    .filter(program -> program.title.equalsIgnoreCase(showTitle))
                    .findFirst()
                    .ifPresent(result::setResult);

            if (!result.hasResult()) {
                result.setErrorResult(new NotFoundException(String.format("The program %s was not found", showTitle)));
            }
        });

        return result;
    }

    public DeferredResult<List<ScheduleDTO>> getScheduledPrograms(String channel) {

        DeferredResult<List<ScheduleDTO>> deferredResult = new DeferredResult<>();

        final ListenableFuture<ResponseEntity<ScheduleDTO[]>> listenableFuture =
                restTemplate.getForEntity("http://www.svt.se/play4api/channel/{channel}/schedule",
                ScheduleDTO[].class, channel);

        listenableFuture.addCallback(result -> {
            deferredResult.setResult(Arrays.asList(result.getBody()));
        }, deferredResult::setErrorResult);

        return deferredResult;

    }

}
