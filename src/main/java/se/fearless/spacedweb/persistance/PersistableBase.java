package se.fearless.spacedweb.persistance;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import se.fearless.spacedweb.persistance.hibernate.UUIDHibernateType;
import se.fearlessgames.common.uuid.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TypeDefs({@TypeDef(name = "uuid", typeClass = UUIDHibernateType.class)})
public abstract class PersistableBase {
	@Id
	@Type(type = "uuid")
	private UUID pk;

	protected PersistableBase(UUID pk) {
		this.pk = pk;
	}

	protected PersistableBase() {
	}


	public UUID getPk() {
		return pk;
	}

	public void setPk(UUID pk) {
		this.pk = pk;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PersistableBase that = (PersistableBase) o;

		if (pk != null ? !pk.equals(that.pk) : that.pk != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return pk != null ? pk.hashCode() : 0;
	}


}
