package vn.thecode.jobhunter.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.thecode.jobhunter.util.constant.LevelEnum;

@Setter
@Getter
public class ResCreateJobDTO {
    private Long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean isActive;
    private List<String> skills;
    private Instant createdAt;
    private String createdBy;

}
