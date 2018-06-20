
namespace java com.example.thrift

struct TUser {
  1: required i64 id;
  2: required string name;
  3: optional i32 age = -1;
}

exception TUserNotFoundException {
  1: required string message;
}

service TUserService {
  TUser createUserWithAge(1:string name, 2:i32 age);
  TUser createUser(1:string name);
  TUser findUserById(1:i64 userId) throws (1:TUserNotFoundException ex);
  TUser removeUser(1:i64 userId) throws (1:TUserNotFoundException ex);
  bool existUser(1:i64 userId);
}
