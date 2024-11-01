package kr.co.cofile.sbimgshop.codegroups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/codegroups")
public class CodeGroupController {

    private final CodeGroupService codeGroupService;

    @PostMapping
    public ResponseEntity<CodeGroupDTO> register(@RequestBody CodeGroupDTO codeGroupDTO) {
        log.info("register: {}", codeGroupDTO);

        codeGroupService.register(codeGroupDTO);

        return ResponseEntity.ok(codeGroupDTO);
    }

}
