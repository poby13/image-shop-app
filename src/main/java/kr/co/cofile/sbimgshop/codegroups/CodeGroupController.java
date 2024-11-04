package kr.co.cofile.sbimgshop.codegroups;

import jakarta.validation.Valid;
import kr.co.cofile.sbimgshop.common.dto.PageDTO;
import kr.co.cofile.sbimgshop.common.exception.CustomValidationException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/codegroups")
public class CodeGroupController {

    private final CodeGroupService codeGroupService;

    // TODO NoResourceFoundException: No static resource codegroups.

    @PostMapping
    public ResponseEntity<CodeGroupDTO> createCodeGroup(@Valid @RequestBody CodeGroupDTO codeGroupDTO,
                                                        BindingResult bindingResult) {
        log.info("register: {}", codeGroupDTO);

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(ErrorCode.VALIDATION_ERROR, bindingResult);
        }

        codeGroupService.createCodeGroup(codeGroupDTO);

        return ResponseEntity.ok(codeGroupDTO);
    }

    @GetMapping
    public ResponseEntity<PageDTO<CodeGroupDTO>> findCodeGroups(@RequestParam(name="page", defaultValue = "1") int page,
                                                                @RequestParam(name="size", defaultValue = "10") int size,
                                                                Model model) {

        PageDTO<CodeGroupDTO> pageDTO = codeGroupService.getCodeGroups(page, size);

        return ResponseEntity.ok(pageDTO);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<PageDTO<CodeGroupDTO>> findCodeGroups(
            @RequestParam(value = "groupCode", required = false) String groupCode,
            @RequestParam(value = "groupName", required = false) String groupName,
            @RequestParam(name="page", defaultValue = "1") int page,
                                                               @RequestParam(name="size", defaultValue = "10") int size,
                                                               Model model) {

        PageDTO<CodeGroupDTO> pageDTO = codeGroupService.getCodeGroups(groupCode, groupName, page, size);

        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/{groupCode}")
    public ResponseEntity<CodeGroupDTO> getCodeGroup(@PathVariable("groupCode") String groupCode) {

         return ResponseEntity.ok(codeGroupService.getCodeGroup(groupCode));
    }
}
