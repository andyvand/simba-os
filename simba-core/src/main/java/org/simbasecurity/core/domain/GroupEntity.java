package org.simbasecurity.core.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SIMBA_GROUP")
public class GroupEntity extends AbstractVersionedEntity implements Group {

    private static final long serialVersionUID = 552484022516217422L;

    @Id
    @GeneratedValue(generator = "simbaSequence", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "simbaSequence", sequenceName = "SEQ_SIMBA_GROUP")
    protected long id = 0;

    @ManyToMany(targetEntity = UserEntity.class, mappedBy = "groups")
    @OrderBy("userName")
    private Set<User> users = new HashSet<User>();

    @ManyToMany(targetEntity = RoleEntity.class)
    @JoinTable(name = "SIMBA_GROUP_ROLE", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @OrderBy("name")
    private Set<Role> roles = new HashSet<Role>();

    private String name;
    private String cn;

    public GroupEntity() {}

    public GroupEntity(String name, String cn) {
        setName(name);
        setCn(cn);
    }

    void addUser(UserEntity userEntity) {
        users.add(userEntity);
    }

    @Override
    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public Collection<User> getUsers() {
        return users;
    }

    @Override
    public void addRole(Role role) {
        roles.add(role);
        ((RoleEntity)role).addGroup(this);
    }

    @Override
    public void addRoles(Collection<Role> roles) {
        for (Role role : roles) {
           addRole(role);
        }
    }

    @Override
    public void removeRole(Role role) {
        roles.remove(role);
        ((RoleEntity)role).removeGroup(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getCN() {
        return cn;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
