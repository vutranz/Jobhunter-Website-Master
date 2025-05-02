package vn.thecode.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thecode.jobhunter.domain.Company;
import vn.thecode.jobhunter.domain.User;
import vn.thecode.jobhunter.domain.response.ResultPaginationDTO;
import vn.thecode.jobhunter.repository.CompanyRepository;
import vn.thecode.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleSaveCompany(Company company) {
        return companyRepository.save(company);
    }

    public void handleRemoveCompanyById(Long id) {
        Optional<Company> comOptional = this.companyRepository.findById(id);
        if (comOptional.isPresent()) {
            Company com = comOptional.get();
            List<User> uList = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(uList);
        }
        companyRepository.deleteById(id);
    }

    public Company handleFindCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }

    public ResultPaginationDTO handleFindAllCompanies(Specification<Company> specification, Pageable pageable) {
        Page<Company> page = this.companyRepository.findAll(specification, pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageNumber());

        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getNumberOfElements());

        paginationDTO.setMeta(mt);
        paginationDTO.setResult(page.getContent());

        return paginationDTO;
    }

    public Company handleModifyCompany(Company company) {
        Company currentCompany = handleFindCompanyById(company.getId());
        currentCompany.setName(company.getName());
        currentCompany.setAddress(company.getAddress());
        currentCompany.setDescription(company.getDescription());
        currentCompany.setLogo(company.getLogo());

        return companyRepository.save(currentCompany);
    }

}
