package cz.itnetwork.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> googleId;
	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, String> profilePicture;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, Role> role;
	public static volatile SingularAttribute<User, String> phone;
	public static volatile SingularAttribute<User, Boolean> oauthUser;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, Boolean> enabled;

	public static final String GOOGLE_ID = "googleId";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PROFILE_PICTURE = "profilePicture";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";
	public static final String PHONE = "phone";
	public static final String OAUTH_USER = "oauthUser";
	public static final String ID = "id";
	public static final String EMAIL = "email";
	public static final String ENABLED = "enabled";

}

