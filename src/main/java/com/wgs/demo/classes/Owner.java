package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "owner", uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "mobile" }))
@Entity
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
	@Id
	private String userId;
	private String name;
	private String mobile;
	private String gender;
	private String pass;
}
