package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller@RequiredArgsConstructor
@EnableScheduling
public class hookController {
    private final Asf4MemberService asf4MemberService;

    @Scheduled(cron = "0 0 10 * * 1-5", zone = "Asia/Tokyo")
    public void hook() {
        asf4MemberService.getTestResponse(asf4MemberService.selectData().getIdobataId());
    }
}
