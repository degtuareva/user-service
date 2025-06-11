package edu.online.messenger.service;

import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.AddressDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.dto.UserInfoDto;
import edu.online.messenger.model.dto.page.PageContentDto;
import edu.online.messenger.model.dto.page.PageParamDto;
import edu.online.messenger.model.entity.dto.AddressFilterDto;

import java.util.List;

public interface UserService {

    boolean existsById(Long id);

    boolean existsByLogin(String login);

    UserDto getUserByLogin(String login);

    UserDto getUserById(Long id);

    List<AddressDto> getAddressListByUserId(Long userId);

    PageContentDto<UserDto> findAll(PageParamDto pageParamDto, AddressFilterDto addressFilterDto);

    UserDto save(UserInfoDto userInfoDto);

    AddressDto addAddressByUserId(AddressCreateDto addressCreateDto);

    void deleteAddressById(Long id);

    void deleteUserById(Long id);
}