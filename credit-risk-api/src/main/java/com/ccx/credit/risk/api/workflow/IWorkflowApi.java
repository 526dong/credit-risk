package com.ccx.credit.risk.api.workflow;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.repository.ProcessDefinition;

public interface IWorkflowApi {
	Map<String,Object> saveStartProcess(HttpServletRequest request);
	
	public String saveSubmitTask(HttpServletRequest request);
	
	public void saverefuseSubmitTask(HttpServletRequest request);
	
	public Map<String, Object> findCoordingByTask(String taskId);
	
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId);
	
	public InputStream findImageInputStream(String deploymentId, String imageName);

    void savePassApproval(Integer approvalId, String adDegree, HttpServletRequest request);
}
