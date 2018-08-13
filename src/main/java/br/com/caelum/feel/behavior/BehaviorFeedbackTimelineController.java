package br.com.caelum.feel.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BehaviorFeedbackTimelineController {

	@Autowired
	private BehaviorFeedbackRepository behaviorFeedbackRepository;

	@GetMapping("/behavior/anonimous/timeline/{hash}")
	public String helloTimeline(Model model, @PathVariable("hash") String hash) {

		Optional<BehaviorFeedback> feedback = behaviorFeedbackRepository.findByHash(hash);

		if (!feedback.isPresent()) {
			model.addAttribute("error",
					"Provavelmente o link que você acessou não está correto. Por conta disso, não conseguimos achar sua mensagem :(. ");
		}

		return "complains/person-timeline";
	}

	@GetMapping("/behavior/anonimous/timeline/messages/{hash}")
	@ResponseBody
	public List<BehaviorFeedbackMessageDTO> messages(Model model,
			@PathVariable("hash") String hash) {

		BehaviorFeedback feedback = behaviorFeedbackRepository.findByHash(hash).get();
		return feedback.asList().stream().map(BehaviorFeedbackMessageDTO::new)
				.collect(Collectors.toList());
	}

	@PostMapping(value = "/behavior/anonimous/timeline/messages/{hash}/append", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ResponseBody
	public String save(@PathVariable("hash") String hash,
			@Valid @RequestBody NewBehaviorFeedbackForm form) {

		BehaviorFeedback root = behaviorFeedbackRepository.findByHash(hash).get();
		BehaviorFeedback newFeedback = form.toBehaviorFeedback();
		behaviorFeedbackRepository.save(newFeedback);
		root.append(newFeedback);

		return "Salvo com sucesso";
	}

}
