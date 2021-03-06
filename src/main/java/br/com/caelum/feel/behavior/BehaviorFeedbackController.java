package br.com.caelum.feel.behavior;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BehaviorFeedbackController {

	@Autowired
	private BehaviorFeedbackRepository behaviorFeedbackRepository;
	@Autowired
	private NewBehaviorFeedbackService newBehaviorFeedbackService;

	@GetMapping("/admin/behavior/feedbacks")
	public String list(Model model) {
		model.addAttribute("list", behaviorFeedbackRepository.findAll());
		return "complains/list";
	}

	@GetMapping("/behavior/feedback/anonimous/form")
	public String form(Model model, NewBehaviorFeedbackForm form,
			@RequestParam(required = false, defaultValue = "true") boolean info) {

		if (info) {
			model.addAttribute("infoMsg",
					"Nos conte o que aconteceu da maneira mais detalhada possível para que possamos entender a situação. \n"
							+ "Nos comprometemos a dar um retorno em até 2 dias úteis.\n"
							+ "Após clicar em enviar, será gerado um link para que você tenha acesso ao nosso retorno e, se necessário,\n"
							+ "conversaremos um pouco mais para ter mais detalhes, tudo de forma anônima caso não se sinta confortável em se \n"
							+ "identificar.");
		}
		return "complains/new-form";
	}

	@GetMapping("/behavior/feedback/anonimous/external/form")
	public String externalForm(Model model, NewBehaviorFeedbackForm form,
			@RequestParam(required = false, defaultValue = "true") boolean info) {

		form(model, form, info);
		return "complains/new-form-external";
	}

	@PostMapping("/behavior/feedback/anonimous")
	public String save(Model model, @Valid NewBehaviorFeedbackForm form,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			if(form.getFeedbackType().equals(BehaviorFeedbackType.INTERNAL)) {
				return form(model, form, true);
			} else {
				return externalForm(model, form, true);
			}
		}

		BehaviorFeedback newFeedback = form.toBehaviorFeedback();
		newBehaviorFeedbackService.execute(newFeedback);

		redirectAttributes.addFlashAttribute("msg", "Seu feedback foi registrado com sucesso. "
				+ " Para que você siga essa conversa, copie o link " + newFeedback.getAccessLink()
				+ ". É importante não perder esse link, pois será nosso único meio de comunicação com você.\n"
				+ "Acesse esse endereço dentro de 2 dias úteis e a gente já vai ter um retorno inicial para você.");

		if (form.getFeedbackType().equals(BehaviorFeedbackType.INTERNAL)) {
			return "redirect:/behavior/feedback/anonimous/form?info=false";
		} else {
			return "redirect:/behavior/feedback/anonimous/external/form?info=false";
		}
	}
}
