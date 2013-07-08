/*
 * Attribution
 * CC BY
 * This license lets others distribute, remix, tweak,
 * and build upon your work, even commercially,
 * as long as they credit you for the original creation.
 * This is the most accommodating of licenses offered.
 * Recommended for maximum dissemination and use of licensed materials.
 *
 * http://creativecommons.org/licenses/by/3.0/
 * http://creativecommons.org/licenses/by/3.0/legalcode
 */
package com.thjug.bgile.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author @nuboat
 */
@Entity
@Table(name = "board")
public class Board extends Time implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	@SequenceGenerator(name = "board_seq_gen", sequenceName = "board_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "statusid")
	private Status statusid;
	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "enableid")
	private Enable enableid;
	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "privateid")
	private Private privateid;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 128)
	@Column(name = "boardname")
	private String boardname;
	@Size(max = 512)
	@Column(name = "description")
	private String description;
	@Size(max = 256)
	@Column(name = "logopath")
	private String logopath;
	@Column(name = "maxcard")
	private int maxcard;

	public Board() {
	}

	public Board(final Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Status getStatusid() {
		return statusid;
	}

	public void setStatusid(final Status statusid) {
		this.statusid = statusid;
	}

	public Enable getEnableid() {
		return enableid;
	}

	public void setEnableid(final Enable enableid) {
		this.enableid = enableid;
	}

	public Private getPrivateid() {
		return privateid;
	}

	public void setPrivateid(final Private privateid) {
		this.privateid = privateid;
	}

	public String getBoardname() {
		return boardname;
	}

	public void setBoardname(final String boardname) {
		this.boardname = boardname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getLogopath() {
		return logopath;
	}

	public void setLogopath(final String logopath) {
		this.logopath = logopath;
	}

	public int getMaxcard() {
		return maxcard;
	}

	public void setMaxcard(int maxcard) {
		this.maxcard = maxcard;
	}

	@Override
	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}

	@Override
	public boolean equals(final Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Board)) {
			return false;
		}
		final Board other = (Board) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Project[ id=" + id + " ]";
	}

}
