package vn.thecode.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thecode.jobhunter.domain.Job;
import vn.thecode.jobhunter.domain.Resume;
import vn.thecode.jobhunter.domain.Skill;
import vn.thecode.jobhunter.domain.User;
import vn.thecode.jobhunter.domain.response.ResultPaginationDTO;
import vn.thecode.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.thecode.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.thecode.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.thecode.jobhunter.repository.JobRepository;
import vn.thecode.jobhunter.repository.ResumeRepository;
import vn.thecode.jobhunter.repository.UserRepository;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository,
            UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Optional<Resume> fetchById(long id) {
        return this.resumeRepository.findById(id);
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null) {
            return false;
        }

        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }

        if (resume.getJob() == null) {
            return false;
        }

        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResCreateResumeDTO dto = new ResCreateResumeDTO();
        dto.setId(resume.getId());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setCreatedBy(resume.getCreatedBy());

        return dto;
    }

    public ResUpdateResumeDTO update(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO dto = new ResUpdateResumeDTO();
        dto.setUpdatedAt(resume.getUpdatedAt());
        dto.setUpdatedBy(resume.getUpdatedBy());

        return dto;
    }

    public void delete(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO dto = new ResFetchResumeDTO();
        dto.setId(resume.getId());
        dto.setEmail(resume.getEmail());
        dto.setUrl(resume.getUrl());
        dto.setStatus(resume.getStatus());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setCreatedBy(resume.getCreatedBy());
        dto.setUpdatedAt(resume.getUpdatedAt());
        dto.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            dto.setCompanyName(resume.getJob().getCompany().getName());
        }
        dto.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        dto.setJob(new ResFetchResumeDTO.JobResume(resume.getUser().getId(), resume.getUser().getName()));

        return dto;
    }

    public ResultPaginationDTO fetchAllResume(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> page = this.resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageNumber());

        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getNumberOfElements());

        paginationDTO.setMeta(mt);
        paginationDTO.setResult(page.getContent());

        List<ResFetchResumeDTO> list = page.getContent().stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());
        paginationDTO.setResult(list);
        return paginationDTO;
    }

}
