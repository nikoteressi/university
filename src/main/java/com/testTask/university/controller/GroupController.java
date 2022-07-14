package com.testTask.university.controller;

import com.testTask.university.dto.GroupDto;
import com.testTask.university.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService service;

    @GetMapping
    public List<GroupDto> getAllGroups() {
        return service.getAllGroups();
    }

    @PostMapping
    public List<GroupDto> createGroup(@RequestBody GroupDto group) {
        return service.createNewGroup(group);
    }

    @PutMapping
    public GroupDto editGroup(@RequestBody GroupDto group) {
        return service.editGroup(group);
    }

    @DeleteMapping("/{groupId}")
    public String removeGroup(@PathVariable long groupId) {
        return service.removeGroup(groupId);
    }
}
