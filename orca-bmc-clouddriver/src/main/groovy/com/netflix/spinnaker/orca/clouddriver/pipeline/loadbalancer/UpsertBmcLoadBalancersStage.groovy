/*
 * copyright 2016 netflix, inc.
 *
 * licensed under the apache license, version 2.0 (the "license");
 * you may not use this file except in compliance with the license.
 * you may obtain a copy of the license at
 *
 *    http://www.apache.org/licenses/license-2.0
 *
 * unless required by applicable law or agreed to in writing, software
 * distributed under the license is distributed on an "as is" basis,
 * without warranties or conditions of any kind, either express or implied.
 * see the license for the specific language governing permissions and
 * limitations under the license.
 */

package com.netflix.spinnaker.orca.clouddriver.pipeline.loadbalancer

import com.netflix.spinnaker.orca.clouddriver.tasks.MonitorKatoTask
import com.netflix.spinnaker.orca.clouddriver.tasks.loadbalancer.UpsertLoadBalancerForceRefreshTask
import com.netflix.spinnaker.orca.clouddriver.tasks.loadbalancer.UpsertLoadBalancerResultObjectExtrapolationTask
import com.netflix.spinnaker.orca.clouddriver.tasks.loadbalancer.UpsertBmcLoadBalancersTask
import com.netflix.spinnaker.orca.pipeline.LinearStage
import com.netflix.spinnaker.orca.pipeline.model.Stage
import groovy.transform.CompileStatic
import org.springframework.batch.core.Step
import org.springframework.stereotype.Component

/*
 * Pipeline Stage for creating multiple load balancers
 */

@Component
@CompileStatic
class UpsertBmcLoadBalancersStage extends LinearStage {

  public static final String PIPELINE_CONFIG_TYPE = "upsertLoadBalancers"

  UpsertBmcLoadBalancersStage() {
    super(PIPELINE_CONFIG_TYPE)
  }

  @Override
  public List<Step> buildSteps(Stage stage) {
    def step1 = buildStep(stage, "upsertBmcLoadBalancers", UpsertBmcLoadBalancersTask)
    def step2 = buildStep(stage, "monitorUpsert", MonitorKatoTask)
    def step3 = buildStep(stage, "extrapolateUpsertResult", UpsertLoadBalancerResultObjectExtrapolationTask)
    def step4 = buildStep(stage, "forceCacheRefresh", UpsertLoadBalancerForceRefreshTask)
    [step1, step2, step3, step4]
  }
}
