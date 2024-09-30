package com.beat.matrimonial.controller;

import com.beat.matrimonial.entity.User;
import com.beat.matrimonial.payload.response.MessageResponse;
import com.beat.matrimonial.payload.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/** The interface User api */
@Tag(name = "User", description = "User API")
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserController {

  @GetMapping()
  @Operation(summary = "Get user", description = "Returns user list", tags = "User APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  public List<User> getAllUsers();


  @GetMapping("/{id}")
  @Operation(summary = "Get user by id", description = "Returns user", tags = "User APIs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  public UserResponse getUserById(@PathVariable Long id);

}
