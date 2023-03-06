package antifraud.service;

import antifraud.exception.IPAlreadyExistException;
import antifraud.exception.IPNotFoundException;
import antifraud.exception.WrongIPFormatException;
import antifraud.model.StatusDTO;
import antifraud.model.ip.IP;
import antifraud.rep.IPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IPService {
    private IPRepository ipRepository;

    @Autowired
    public IPService(IPRepository ipRepository) {
        this.ipRepository = ipRepository;
    }


    public IP addIP(IP ip) {
        if (!checkIPFormat(ip)) {
            throw new WrongIPFormatException();
        }
        if (ipRepository.findByIp(ip.getIp()).isPresent()) {
            throw new IPAlreadyExistException();
        }
        return ipRepository.save(ip);
    }

    public StatusDTO deleteIP(String ip) {
        if (!checkIPFormat(ip)) {
            throw new WrongIPFormatException();
        }
        IP ipInstance = ipRepository.findByIp(ip).orElseThrow(IPNotFoundException::new);
        ipRepository.delete(ipInstance);
        return new StatusDTO("IP %s successfully removed!".formatted(ip));
    }

    public List<IP> getAllIPs() {
        return (ArrayList<IP>) ipRepository.findAll();
    }

    public boolean isExist(String ip) {
        return ipRepository.existsByIp(ip);
    }

    public IP findIP(String ip) {
        return ipRepository.findByIp(ip).orElseThrow(IPNotFoundException::new);
    }

    public boolean checkIPFormat(String ip) {
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
    }

    public boolean checkIPFormat(IP ip) {
        return checkIPFormat(ip.getIp());
    }
}
