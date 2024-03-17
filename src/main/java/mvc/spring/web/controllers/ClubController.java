package mvc.spring.web.controllers;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import mvc.spring.web.dto.ClubDto;
import mvc.spring.web.services.impl.ClubService;

@Controller
@RequestMapping("/clubs")
public class ClubController {

    private ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/all")
    public String listClubs(@NotNull Model model) {
        List<ClubDto> clubs = clubService.findAll();
        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }

    @GetMapping("/new")
    public String createClubForm(@NotNull Model model) {
        ClubDto club = new ClubDto();
        model.addAttribute("club", club);
        return "clubs-create";
    }

    @PostMapping("/new")
    public String saveClub(@Valid @ModelAttribute("club") ClubDto club,
            BindingResult result) {

        if (result.hasErrors()) {
            return "clubs-create";
        }
        clubService.save(club);
        return "redirect:/clubs/all";

    }

    @GetMapping("/{clubId}/edit")
    public String editClubForm(@PathVariable("clubId") long clubId,
            @NotNull Model model) throws NotFoundException {
        ClubDto club = clubService.findById(clubId);
        model.addAttribute("club", club);
        return "clubs-edit";
    }

    @PostMapping("/{clubId}/edit")
    public String updateClub(@PathVariable("clubId") long clubId,
            @Valid @ModelAttribute("club") ClubDto club,
            BindingResult result) {

        if (result.hasErrors()) {
            return "clubs-edit";
        }
        club.setId(clubId);
        clubService.update(club);
        return "redirect:/clubs/all";
    }

    @GetMapping("/{clubId}")
    public String clubDetail(@PathVariable("clubId") long clubId, Model model) throws NotFoundException {

        ClubDto club = clubService.findById(clubId);
        model.addAttribute("club", club);

        return "clubs-detail";

    }

    @GetMapping("/{clubId}/delete")
    public String deleteClub(@PathVariable long clubId) {
        clubService.deleteById(clubId);
        return "redirect:/clubs/all";

    }

}
