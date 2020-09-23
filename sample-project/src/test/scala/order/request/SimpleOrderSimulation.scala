package order.request

import io.gatling.core.Predef._ 
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SimpleOrderSimulation extends Simulation { 
  val camundaAPI = http 
    .baseUrl("http://localhost/engine-rest") 


  val simple = scenario("Simple Process") 
    .exec(_.set("businessKey", java.util.UUID.randomUUID.toString()))
    .exec(http("Create Deployment")
      .post("/deployment/create")
      .header("Content-Type", "multipart/form-data;")
      .formParam("deployment-name", "gatling-test")
      .formParam("enable-duplicate-filtering", "true")
      .formParam("deployment-source", "Gatling Test")
      .formUpload("data", "../../main/bpmn/simple_process.bpmn")
      .check(
        status.is(200),
        jsonPath("$.id").saveAs("deploymentId")
      )
    )
    .exec(http("Query Process Definition Key") 
      .get("/process-definition?deploymentId=${deploymentId}")
      .check(
        status.is(200), 
        jsonPath("$[0].id").saveAs("processDefinitionKey")
      )
    ).pause(1)
    .exec(http("Submit Process") 
      .post("/process-definition/${processDefinitionKey}/submit-form")
      .header("Content-Type", "application/json;charset=UTF-8")
      .body(ElFileBody("bodies/simple_payload.json"))
      .check(
        status.is(200), 
        jsonPath("$.businessKey").is("${businessKey}"),
        jsonPath("$.ended").is("false"),
        jsonPath("$.suspended").is("false"),
        jsonPath("$.id").saveAs("processInstanceId")
      )
    ).pause(5) 
    .exec(http("Process History") 
      .get("/history/process-instance?processInstanceId=${processInstanceId}")
      .check(
        status.is(200), 
        jsonPath("$[0].id").is("${processInstanceId}"),
        jsonPath("$[0].businessKey").is("${businessKey}"),
        jsonPath("$[0].state").is("COMPLETED")
      )
    ) 
    .exec(http("Process Variables") 
      .get("/history/variable-instance?processInstanceId=${processInstanceId}")
      .check(
        status.is(200), 
        jsonPath("$[?(@.name=='messageApproved')].value").is("true")
      )
    ) 

  setUp( 
    simple.inject(atOnceUsers(1)) 
  ).protocols(camundaAPI) 
}