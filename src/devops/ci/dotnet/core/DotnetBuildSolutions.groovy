package devops.ci.dotnet.core
import devops.ci.*

class DotnetBuildProjects implements Serializable {

  private Object solutions
  private String parameters = "-configuration Release --verbosity normal"
  private def pipeline

  DotnetBuildProjects(def pipeline){
    this.pipeline = pipeline
  }

  public DotnetBuildProjects setSolutions(Object solutions){
    this.solutions = solutions
    return this
  }

  public DotnetBuildProjects setParameters(String parameters){
    this.parameters = parameters
    return this
  }

  public void buildSolutions(){

    for(solution in solutions){

      if(this.pipeline.fileExists(solution.path)){

          def command = "dotnet build ${solution.path} "
          command += solution.buildParameters ? olution.buildParameters : this.parameters

          this.pipeline.println('$> ' + command)
          def solutionBuildStatus = this.pipeline.sh(
            script: command,
            returnStatus: true
          )
          this.pipeline.println('$> ' + solutionBuildStatus)
      } else {
          this.pipeline.error("FAILED: Build solution file not found: ${solution.path}")
      }
    }
  }
}