export interface User {
  username: string;
  phone: string;
  email: string;
  userType: 'SIMPLE' | 'SPECIAL' | 'ADMIN' | 'LONG_TERM';
  password: string;
}