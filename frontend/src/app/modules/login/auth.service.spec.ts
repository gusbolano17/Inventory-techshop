import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { of } from 'rxjs';
import { AuthService } from './auth.service';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';


describe('AuthService', () => {

  TestBed.configureTestingModule({
    providers: [provideHttpClientTesting()],
  });

  let service: AuthService;
  let mockHttp: {
    post: ReturnType<typeof vi.fn>;
  };
  let mockRouter: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    mockHttp = {
      post: vi.fn(),
    };

    mockRouter = {
      navigate: vi.fn(),
    };

    service = new AuthService(mockHttp as never, mockRouter as never);
  });

  afterEach(() => {
    vi.clearAllMocks();
    sessionStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('isAuthenticated', () => {
    it('should return true when token exists', () => {
      sessionStorage.setItem('accessToken', 'valid-token');
      const newService = new AuthService(mockHttp as never, mockRouter as never);
      expect(newService.isAuthenticated()).toBe(true);
    });

    it('should return false when no token exists', () => {
      sessionStorage.clear();
      const newService = new AuthService(mockHttp as never, mockRouter as never);
      expect(newService.isAuthenticated()).toBe(false);
    });
  });

  describe('token getter', () => {
    it('should return token from sessionStorage', () => {
      sessionStorage.setItem('accessToken', 'my-token');
      expect(service.token).toBe('my-token');
    });

    it('should return null when no token exists', () => {
      sessionStorage.clear();
      expect(service.token).toBeNull();
    });
  });

  describe('login', () => {
    it('should call login endpoint with correct data', () => {
      const loginDto = { email: 'test@example.com', password: 'password' };
      const mockResponse = { accessToken: 'token', refreshToken: 'refresh' };

      mockHttp.post.mockReturnValue(of(mockResponse));

      service.login(loginDto).subscribe((response) => {
        expect(response.accessToken).toBe('token');
      });

      expect(mockHttp.post).toHaveBeenCalledWith('http://localhost:8090/api/auth/login', loginDto);
    });

    it('should store tokens in sessionStorage after login', () => {
      const loginDto = { email: 'test@example.com', password: 'password' };
      const mockResponse = { accessToken: 'new-token', refreshToken: 'new-refresh' };

      mockHttp.post.mockReturnValue(of(mockResponse));

      service.login(loginDto).subscribe();

      expect(sessionStorage.getItem('accessToken')).toBe('new-token');
      expect(sessionStorage.getItem('refreshToken')).toBe('new-refresh');
    });
  });

  describe('logout', () => {
    it('should clear tokens and redirect to login', () => {
      sessionStorage.setItem('accessToken', 'some-token');
      sessionStorage.setItem('refreshToken', 'some-refresh');

      mockHttp.post.mockReturnValue(of({}));

      service.logout().subscribe();

      expect(sessionStorage.getItem('accessToken')).toBeNull();
      expect(sessionStorage.getItem('refreshToken')).toBeNull();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    });
  });
});
