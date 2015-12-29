package boot.service;

import boot.dto.ScheduleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TVService {

    private RestTemplate restTemplate = new RestTemplate();

    public ScheduleDTO nextOnAir(String showTitle) {
        // add logic to determine if the given show is on TV today
        return null;
    }

    public List<ScheduleDTO> getScheduledPrograms(String channel) {
        return Arrays.asList(restTemplate.getForObject("http://www.svt.se/play4api/channel/{channel}/schedule",
                ScheduleDTO[].class, channel));
    }

}
