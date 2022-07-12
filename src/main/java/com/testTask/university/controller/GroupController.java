package com.testTask.university.controller;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService service;

    @GetMapping("/all-groups")
    public List<GroupDto> getAllGroups() {
        return service.getAllGroups();
    }

    @PostMapping("/new-group")
    public List<GroupDto> createGroup(@RequestBody GroupDto group) throws Exception {
        return service.createNewGroup(group);
    }

    @PutMapping("/edit-group")
    public GroupDto editGroup(@RequestBody GroupDto group) throws Exception {
        return service.editGroup(group);
    }

    @DeleteMapping("/remove-group")
    public String removeGroup(@RequestParam long groupId) throws Exception {
        return service.removeGroup(groupId);
    }
}
