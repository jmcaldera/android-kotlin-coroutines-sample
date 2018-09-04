package com.jmcaldera.cattos.features

import com.jmcaldera.cattos.common.ArchComponentTest
import com.jmcaldera.cattos.common.TestDispatcher
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.domain.functional.right
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as given
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class CatsViewModelTest : ArchComponentTest() {

  @Mock private lateinit var cattoRepository: CattoRepository

  private lateinit var viewModel: CatsViewModel

  @Before
  fun setup() {
    viewModel = CatsViewModel(TestDispatcher(), cattoRepository)
  }

  @Test
  fun testGetCats() = runBlocking<Unit> {
    given(cattoRepository.getCatImages()).thenReturn(right(emptyList()))
    viewModel.getCats()
    verify(cattoRepository, times(1)).getCatImages()
  }

}