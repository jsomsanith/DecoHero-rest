package com.jso.deco.data.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.jso.deco.data.api.DBFriend;
import com.jso.deco.data.api.DBUser;
import com.jso.deco.data.api.DBUserInfos;

@Repository
public class UserDataService {
	public static final String ID = "id";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String DELETION_DATE = "deletionDate";
	public static final String EMAIL = "email";
	public static final String AVATAR = "avatar";

	private MongoTemplate mongoTemplate;

	/**
	 * Create a user in database
	 * @param user
	 */
	public void create(DBUser user) {
		user.setCreationDate(new Date());
		mongoTemplate.save(user);
	}
	
	/**
	 * Update a user in database
	 * @param user
	 */
	public void update(DBUser user) {
		user.setModificationDate(new Date());
		mongoTemplate.save(user);
	}
	
	/**
	 * Delete a user in database
	 * @param user
	 */
	public void delete(DBUser user) {
		user.setDeletionDate(new Date());
		mongoTemplate.save(user);
	}
	
	/**
	 * Find user by id
	 * @param id
	 * @return
	 */
	public DBUser findById(String id) {
		Criteria idCriteria = Criteria.where(ID).is(id);
		Query searchUserQuery = new Query(idCriteria);
		return mongoTemplate.findOne(searchUserQuery, DBUser.class);
	}
	
	/**
	 * Find user infos by id
	 * @param id
	 * @return
	 */
	public DBUserInfos findInfosById(String id) {
		Criteria idCriteria = Criteria.where(ID).is(id);
		Query searchUserQuery = new Query(idCriteria);
		return mongoTemplate.findOne(searchUserQuery, DBUserInfos.class);
	}
	
	/**
	 * Find users infos by set of ids
	 * @param userIds
	 * @return
	 */
	public List<DBUserInfos> findInfosByIds(Set<String> userIds) {
		Criteria idCriteria = Criteria.where(ID).in(userIds);
		Query searchUserQuery = new Query(idCriteria);
		return mongoTemplate.find(searchUserQuery, DBUserInfos.class);
	}
	
	/**
	 * Find user from email/encrypted_password
	 * @param email
	 * @param password
	 * @return
	 */
	public DBUser find(String email, String password) {
		Criteria emailCriteria = Criteria.where(EMAIL).is(email);
		Criteria passwordCriteria = Criteria.where(PASSWORD).is(password);
		Criteria notDeletedCriteria = Criteria.where(DELETION_DATE).is(null);
		
		Criteria searchUserCriteria = new Criteria().andOperator(emailCriteria, passwordCriteria, notDeletedCriteria);
		Query searchUserQuery = new Query(searchUserCriteria);
		return mongoTemplate.findOne(searchUserQuery, DBUser.class);
	}
	
	/**
	 * Test if a username already exists
	 * @param username
	 * @return
	 */
	public boolean exists(String field, String value) {
		Criteria idCriteria = Criteria.where(field).is(value);
		Query searchUserQuery = new Query(idCriteria);
		return mongoTemplate.exists(searchUserQuery, DBUser.class);
	}
	
	/**
	 * Test if user avatar is NOT defined
	 * @param userId
	 * @return
	 */
	public boolean userAvatarEmpty(String userId) {
		Criteria idCriteria = Criteria.where(ID).is(userId);
		Criteria avatarEmptyCriteria = Criteria.where(AVATAR).is(null);
		
		Criteria searchUserCriteria = new Criteria().andOperator(idCriteria, avatarEmptyCriteria);
		Query searchUserQuery = new Query(searchUserCriteria);
		return mongoTemplate.exists(searchUserQuery, DBUser.class);
	}

	/**
	 * Update user avatar
	 * @param userId
	 * @param imageEncodedId
	 */
	public void updateAvatar(String userId, String imageEncodedId) {
		DBUser user = findInfosById(userId);
		user.setAvatar(imageEncodedId);
		user.setModificationDate(new Date());
		mongoTemplate.save(user);
	}
	
	/**
	 * Find friends infos by ids
	 * @param friendsIds
	 * @return
	 */
	public List<DBFriend> findFriends(final List<String> friendsIds) {
		Criteria idCriteria = Criteria.where(ID).in(friendsIds);
		Query searchFriendsQuery = new Query(idCriteria);
		return mongoTemplate.find(searchFriendsQuery, DBFriend.class);
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
