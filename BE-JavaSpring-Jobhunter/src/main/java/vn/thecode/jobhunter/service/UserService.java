package vn.thecode.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thecode.jobhunter.domain.Company;
import vn.thecode.jobhunter.domain.User;
import vn.thecode.jobhunter.domain.response.ResCreateUserDTO;
import vn.thecode.jobhunter.domain.response.ResUpdateUserDTO;
import vn.thecode.jobhunter.domain.response.ResUserDTO;
import vn.thecode.jobhunter.domain.response.ResultPaginationDTO;
import vn.thecode.jobhunter.repository.UserRepository;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleSaveUser(User user) {
        if (user.getCompany() != null) {
            Company compOptional = this.companyService.handleFindCompanyById(user.getCompany().getId());
            user.setCompany(compOptional);
        }
        return userRepository.save(user);
    }

    public void handleRemoveUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleFindUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO handleFindAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> page = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getNumberOfElements());

        paginationDTO.setMeta(mt);
        List<ResUserDTO> listUser = page.getContent()
                .stream()
                .map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt(),
                        item.getCompany() != null
                                ? new ResUserDTO.CompanyUser(item.getCompany().getId(), item.getCompany().getName())
                                : null))
                .collect(Collectors.toList());

        paginationDTO.setResult(listUser);

        return paginationDTO;
    }

    public User handleModifyUser(User user) {
        User currentUser = this.handleFindUserById(user.getId());
        if (currentUser != null) {
            currentUser.setAddress(user.getAddress());
            currentUser.setGender(user.getGender());
            currentUser.setAge(user.getAge());
            currentUser.setName(user.getName());

            if (user.getCompany() != null) {
                Company compOptional = this.companyService.handleFindCompanyById(user.getCompany().getId());
                user.setCompany(compOptional);
            }
            this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handldeGetUserByUserName(String userName) {
        return this.userRepository.findByEmail(userName);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO dto = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            dto.setCompany(com);
        }
        return dto;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getUpdatedAt());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            res.setCompany(com);
        }

        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handldeGetUserByUserName(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
