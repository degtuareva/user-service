package edu.online.messenger.service.impl;

import edu.online.messenger.exception.UserNotFoundException;
import edu.online.messenger.mapper.UserMapper;
import edu.online.messenger.model.dto.AddressCreateDto;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.repository.AddressRepository;
import edu.online.messenger.repository.UserRepository;
import edu.online.messenger.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final AddressRepository addressRepository;

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    @Override
    public UserDto getUserDtoByLogin(String login) {
        return userMapper.toDto(repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }


    @Transactional
    public void removeAddress(@PathVariable AddressCreateDto addressCreateDto) {
        Long addressId = addressCreateDto.getId();
        if (addressId != null && addressRepository.existsById(addressId)) {
            addressRepository.deleteById(addressId);
        }
    }
}


//    **Разработать API по отвязке адреса от пользователя.
//    На входе AddressCreateDto, на выходе ничего нет.**
//
//            - Материал для работы
//
//    https://www.baeldung.com/spring-controller-vs-restcontroller
//
//    https://www.baeldung.com/spring-requestmapping
//
//    https://javarush.com/quests/lectures/questspring.level04.lecture16
//
//    https://www.baeldung.com/spring-response-status
//
//    https://habr.com/ru/articles/500572/
//
//    https://www.baeldung.com/spring-mvc-tutorial
//
//    https://sky.pro/media/raznicza-mezhdu-requestparam-i-pathvariable-v-java/
//
//    https://www.geeksforgeeks.org/spring-boot-jparepository-with-example/
//
//    https://habr.com/ru/articles/682362/
//
//
//    Все необходимые модели и dto уже присутствуют в проекте.
//
//    В данную задачу входит разработка методов контроллера, сервиса и репозитория, т.е все необходимое для функционирования эндпоинта.
//
//            1. **Контроллер**
//
//            *UserController* (корневой контекстный путь - /api/users).
//
//    Необходимо разработать если его еще нет.
//
//            2. **Сервис**
//
//            *UserService*.
//
//    Не должен выбрасывать никаких исключений.
//
//    Не забываем про транзакции.
//
//            3. **Репозиторий**:
//
//    UserRepository.
//
//    Должен быть разработан на основе JpaRepository (если он еще не был разработан).
//
//
//            **Пример**:
//
//    DELETE-запрос - http://localhost:9082/api/users/address/7
//
//    Ответ - 204 код
//
//    После завершения задачи в файле CHANGELOG[.](http://README.MD)md необходимо кратко описать разработанный эндпоинт в формате :
//
//            > ## Контроллер → ### Вид запроса(Get, Post …) → #### Суть эндпоинта (получение по id …) → Запрос (http://…) →  Тело запроса (если есть) → Ответ → Возможные исключения (если есть)
//            >
//



