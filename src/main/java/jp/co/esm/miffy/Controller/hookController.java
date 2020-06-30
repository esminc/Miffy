package jp.co.esm.miffy.Controller;

import ajd4jp.AJDException;
import ajd4jp.Holiday;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.ZoneId;

import static ajd4jp.iso.AJD310.now;

@Controller@RequiredArgsConstructor
@EnableScheduling
public class hookController {
    private final Asf4MemberService asf4MemberService;

    @Scheduled(cron = "0 0 10 * * 1-5", zone = "Asia/Tokyo")
    public void hook() throws AJDException {
        Holiday holiday = Holiday.getHoliday(now(ZoneId.systemDefault()));
        if(holiday == null) {
            asf4MemberService.postToHook(asf4MemberService.getCleaner().getIdobataId());
        }
    }
}
