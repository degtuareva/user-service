package edu.online.messenger.service;

import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.PageContentDto;
import edu.online.messenger.model.dto.PageParamDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.dto.AddressFilterDto;
import edu.online.messenger.model.dto.UserInfoDto;

import java.util.List;

public interface UserService {

    boolean existsById(Long id);

    boolean existsByLogin(String login);

    UserDto getUserDtoByLogin(String login);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    UserDto saveUser(UserInfoDto userInfoDto);

    AddressDto addAddressToUser(AddressCreateDto addressCreateDto);

    List<AddressDto> getAddressesByUserId(Long userId);

    void deleteAddressById(Long id);

    PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto);
}